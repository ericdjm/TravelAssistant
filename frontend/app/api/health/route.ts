import { NextResponse } from 'next/server';

export async function GET() {
  try {
    // Basic health check for frontend
    return NextResponse.json({ 
      status: 'healthy', 
      service: 'travel-assistant-frontend',
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    return NextResponse.json(
      { status: 'unhealthy', error: 'Service check failed' },
      { status: 500 }
    );
  }
}